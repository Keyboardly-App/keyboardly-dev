package com.rowland.kokokeyboard.manager;

import android.view.inputmethod.InputConnection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KeyboardManagerTest {

    private KeyboardManager keyboardManager;

    @Mock
    private InputConnection inputConnection;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        keyboardManager = Mockito.spy(new KeyboardManager(inputConnection));

        Mockito.doReturn("QWERT").when(inputConnection).getTextBeforeCursor(100, 0);
        Mockito.doReturn("ABCD").when(inputConnection).getTextAfterCursor(100, 0);
    }

    @Test
    public void handleKeyStrokeChar() {
        keyboardManager.onKeyStroke('C');
        Mockito.verify(keyboardManager, Mockito.times(1)).handleKeyStroke('C');
    }

    @Test
    public void handleKeyStrokeSpecialKey() {
        keyboardManager.onKeyStroke('C');
        keyboardManager.onKeyStroke(KeyboardManager.KEYCODE_BACKSPACE, false);
        Mockito.verify(keyboardManager, Mockito.times(1)).handleKeyStroke(KeyboardManager.KEYCODE_BACKSPACE, false);

        keyboardManager.handleKeyStroke(KeyboardManager.KEYCODE_SPACE, false);
        Mockito.verify(keyboardManager, Mockito.times(1)).handleKeyStroke(KeyboardManager.KEYCODE_SPACE, false);
    }

    @Test
    public void clearAll() {
        keyboardManager.onKeyStroke('C');
        keyboardManager.onKeyStroke(KeyboardManager.KEYCODE_BACKSPACE, true);
        Mockito.verify(keyboardManager, Mockito.times(1)).clearAll();
    }

    @Test
    public void registerListener() {
        KeyboardManager.KeyboardListener keyboardListener = Mockito.mock(KeyboardManager.KeyboardListener.class);
        keyboardManager.registerListener(keyboardListener);

        keyboardManager.onKeyStroke('C');
        Mockito.verify(keyboardListener, Mockito.times(1)).characterClicked('C');

        keyboardManager.onKeyStroke(KeyboardManager.KEYCODE_BACKSPACE, true);
        Mockito.verify(keyboardListener, Mockito.times(1)).specialKeyClicked(KeyboardManager.KEYCODE_BACKSPACE);
    }
}