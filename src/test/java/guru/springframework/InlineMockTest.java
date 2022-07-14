package guru.springframework;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class InlineMockTest {

    @Test
    void testInlineMock() {
        Map mapMock = mock(Map.class);
        assertNotNull(mapMock);
        assertEquals(mapMock.size(), 0);
    }
}
