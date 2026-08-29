package com.ejemplo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {
    @Test
    void testSumar() {
        Calculadora calc = new Calculadora();
        assertEquals(8, calc.sumar(5, 3));
    }
    @Test
    void testRestar() {
        Calculadora calc = new Calculadora();
        assertEquals(6, calc.restar(10, 4));
    }
}