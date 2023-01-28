package app.keyboardly.dev.keyboard.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import app.keyboardly.dev.keyboard.keys.RectangularKeyView;
import app.keyboardly.dev.keyboard.keys.SquareKeyView;
import app.keyboardly.dev.keyboard.manager.KeyboardManager;
import timber.log.Timber;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class KeyboardLayout extends LinearLayout {

    private final KeyboardManager keyboardManager;

    public KeyboardLayout(Context context, KeyboardManager keyboardManager) {
        super(context);
        this.keyboardManager = keyboardManager;
        init();
    }

    public KeyboardLayout(Context context, KeyboardManager keyboardManager, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.keyboardManager = keyboardManager;
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void registerListener(KeyboardManager.KeyboardListener listener) {
        keyboardManager.registerListener(listener);
        setKeypadClickListener(this);
    }


    public void setKeypadClickListener(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                setKeypadClickListener((ViewGroup) child);
            } else {
                if (child != null) {
                    child.setOnClickListener(view -> {

                        if (view instanceof SquareKeyView) {
                            char text = ((Button) view).getText().charAt(0);
                            keyboardManager.onKeyStroke(text);
                        } else if (view instanceof RectangularKeyView) {
                            boolean isSpecialKey = ((RectangularKeyView) view).isSpecialKey();
//                            Timber.d("is spesical key ="+isSpecialKey);
                            if (isSpecialKey) {
                                keyboardManager.onKeyStroke(((RectangularKeyView) view).getSpecialKeyCode(), false);
                            } else {
                                char text = ((Button) view).getText().charAt(0);
                                keyboardManager.onKeyStroke(text);
                            }
                        }
                    });

                    if (child instanceof RectangularKeyView) {
                        if (((RectangularKeyView) child).isSpecialKey()) {
                            child.setLongClickable(true);
                            child.setOnLongClickListener(v -> {
                                keyboardManager.onKeyStroke(((RectangularKeyView) child).getSpecialKeyCode(), true);
                                return true;
                            });
                        }
                    } else if (child instanceof AppCompatImageView) {
                        child.setClickable(true);
                        child.setOnClickListener(v -> keyboardManager.onButtonClick(child.getId()));
                    }
                }
            }
        }
    }
}
