package chapters.ch3;

import chapters.ch2.factory.FittingParametersFactory;
import chapters.ch2.implem.function_fitting.FittingParameters;
import chapters.ch2.implem.function_fitting.TabularMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestTabularMemory {

    private FittingParameters parameters;
    private TabularMemory tabularMemory;

    @BeforeEach
     void setup() {
        parameters = FittingParametersFactory.produceDefault();
        tabularMemory = TabularMemory.of(parameters);
    }

    @Test
     void testReadDefaultMemoryValue() {
        double defaultValue = parameters.defaultMemoryValue();
        assertEquals(defaultValue, tabularMemory.read(0));
    }

    @Test
     void testWriteAndRead() {
        int index = 0;
        double value = 10.0;
        tabularMemory.write(index, value);
        assertEquals(value, tabularMemory.read(index));
    }

    @Test
     void testSize() {
        assertEquals(0, tabularMemory.size());
        tabularMemory.write(0, 10.0);
        assertEquals(1, tabularMemory.size());
        tabularMemory.write(1, 20.0);
        assertEquals(2, tabularMemory.size());
    }

    @Test
     void testMultipleWrites() {
        int index1 = 0;
        int index2 = 1;
        double value1 = 10.0;
        double value2 = 20.0;
        tabularMemory.write(index1, value1);
        tabularMemory.write(index2, value2);
        assertEquals(value1, tabularMemory.read(index1));
        assertEquals(value2, tabularMemory.read(index2));
    }

    @Test
     void testReadNonExistingIndex() {
        int index = 10;
        double defaultValue = parameters.defaultMemoryValue();
        assertEquals(defaultValue, tabularMemory.read(index));
    }
    
}
