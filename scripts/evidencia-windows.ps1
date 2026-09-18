$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

$deployRoot = Join-Path $projectRoot "deploy-windows"
$artifact = Join-Path $projectRoot "target\inventario-automatizado-1.0.0.jar"

function Invoke-HealthCheck {
    param([Parameter(Mandatory = $true)][string]$JarPath)

    $health = (& java -jar $JarPath health | Out-String).Trim()
    if ($LASTEXITCODE -ne 0 -or $health -ne "HEALTHY") {
        throw "Health check fallido para $JarPath. Respuesta: $health"
    }
    return $health
}

function Invoke-BlueGreenDeploy {
    $activeFile = Join-Path $deployRoot "active-slot.txt"
    $previousFile = Join-Path $deployRoot "previous-slot.txt"
    $active = if (Test-Path $activeFile) {
        (Get-Content $activeFile -Raw).Trim()
    } else {
        "none"
    }
    $target = if ($active -eq "blue") { "green" } else { "blue" }
    $targetDirectory = Join-Path $deployRoot $target
    $targetJar = Join-Path $targetDirectory "app.jar"

    New-Item -ItemType Directory -Force -Path $targetDirectory | Out-Null
    Copy-Item -Force $artifact $targetJar
    $health = Invoke-HealthCheck -JarPath $targetJar

    if ($active -ne "none") {
        Set-Content -Path $previousFile -Value $active
    }
    Set-Content -Path $activeFile -Value $target
    Write-Host "DEPLOY_OK strategy=blue-green previous=$active active=$target health=$health" -ForegroundColor Green
}

function Invoke-SmokeTest {
    $activeFile = Join-Path $deployRoot "active-slot.txt"
    if (-not (Test-Path $activeFile)) {
        throw "SMOKE_FAILED: no existe un slot activo"
    }
    $active = (Get-Content $activeFile -Raw).Trim()
    $activeJar = Join-Path (Join-Path $deployRoot $active) "app.jar"
    $health = Invoke-HealthCheck -JarPath $activeJar
    Write-Host "SMOKE_OK active=$active health=$health" -ForegroundColor Green
}

function Invoke-Rollback {
    $activeFile = Join-Path $deployRoot "active-slot.txt"
    $previousFile = Join-Path $deployRoot "previous-slot.txt"
    if (-not (Test-Path $previousFile)) {
        throw "ROLLBACK_NOT_AVAILABLE: no existe un slot anterior"
    }

    $current = (Get-Content $activeFile -Raw).Trim()
    $previous = (Get-Content $previousFile -Raw).Trim()
    $previousJar = Join-Path (Join-Path $deployRoot $previous) "app.jar"
    $health = Invoke-HealthCheck -JarPath $previousJar

    Set-Content -Path $activeFile -Value $previous
    Set-Content -Path $previousFile -Value $current
    Write-Host "ROLLBACK_OK from=$current active=$previous health=$health" -ForegroundColor Green
}

Write-Host "=== EVIDENCIA 3: DEPLOYMENT PIPELINE EN WINDOWS ===" -ForegroundColor Cyan
Write-Host "[1/5] Build del artefacto" -ForegroundColor Yellow
& mvn -B clean package "-DskipUnitTests=true" "-DskipIntegrationTests=true" "-DskipAcceptanceTests=true"
if ($LASTEXITCODE -ne 0) {
    throw "El build Maven finalizo con error"
}
if (-not (Test-Path $artifact)) {
    throw "No se encontro el artefacto $artifact"
}

if (Test-Path $deployRoot) {
    Remove-Item -Recurse -Force $deployRoot
}
New-Item -ItemType Directory -Force -Path $deployRoot | Out-Null

Write-Host "[2/5] Primer despliegue al slot inactivo" -ForegroundColor Yellow
Invoke-BlueGreenDeploy
Write-Host "[3/5] Segundo despliegue y cambio de slot" -ForegroundColor Yellow
Invoke-BlueGreenDeploy
Write-Host "[4/5] Smoke test antes del rollback" -ForegroundColor Yellow
Invoke-SmokeTest
Write-Host "[5/5] Rollback y verificacion final" -ForegroundColor Yellow
Invoke-Rollback
Invoke-SmokeTest

Write-Host "EVIDENCIA_WINDOWS_OK blue-green=OK smoke=OK rollback=OK" -ForegroundColor Cyan
