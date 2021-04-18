package com.vha.game;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.myscript.iink.Configuration;
import com.myscript.iink.ContentBlock;
import com.myscript.iink.ContentPackage;
import com.myscript.iink.ContentPart;
import com.myscript.iink.Editor;
import com.myscript.iink.Engine;
import com.myscript.iink.IEditorListener;
import com.myscript.iink.MimeType;
import com.myscript.iink.uireferenceimplementation.EditorView;
import com.myscript.iink.uireferenceimplementation.FontUtils;
import com.myscript.iink.uireferenceimplementation.InputController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, IEditorListener {
    View responseView;
    ImageButton microphoneButton;
    private EditorView editorView;
    private ContentPackage contentPackage;
    private ContentPart contentPart;
    private Button send;
    private String answer;

    public static final String TAG = "AndroidLauncher";

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private Engine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout layout = new RelativeLayout(this);
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        engine = VibeHeroApplication.getEngine();

        // configure recognition
        Configuration conf = engine.getConfiguration();
        String confDir = "zip://" + getPackageCodePath() + "!/assets/conf";
        conf.setStringArray("configuration-manager.search-path", new String[]{confDir});
        String tempDir = getFilesDir().getPath() + File.separator + "tmp";
        conf.setString("content-package.temp-folder", tempDir);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View gameView = initializeForView(new VibeHero(this, height, width), config);

        LayoutInflater inflater = LayoutInflater.from(this);
        responseView = inflater.inflate(R.layout.response_select, null);

        microphoneButton = responseView.findViewById(R.id.microphone);
        microphoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startVoiceRecognitionActivity();
            }
        });

        send = responseView.findViewById(R.id.button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentBlock block = editorView.getEditor().getRootBlock();
                try {
                    String result = editorView.getEditor().export_(block, MimeType.TEXT);
                    System.out.println("Answer tried: " + result);
                    System.out.println("Answer: " + answer);
                    editorView.getEditor().clear();
                    if(answer.equals(result)) {
                        responseView.setVisibility(View.GONE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        layout.addView(gameView);
        layout.addView(responseView);

        setContentView(layout);

        editorView = findViewById(R.id.editor_view);

        AssetManager assetManager = getApplicationContext().getAssets();
        Map<String, Typeface> typefaceMap = FontUtils.loadFontsFromAssets(assetManager);
        editorView.setTypefaces(typefaceMap);

        editorView.setEngine(engine);


        final Editor editor = editorView.getEditor();
        editor.addListener(this);
        editor.setTheme("stroke { color: #f1fc20; }, guide {}");

        editorView.setInputMode(InputController.INPUT_MODE_FORCE_PEN); // If using an active pen, put INPUT_MODE_AUTO here

        String packageName = "File1.iink";
        File file = new File(getFilesDir(), packageName);
        try {
            contentPackage = engine.createPackage(file);
            // Choose type of content (possible values are: "Text Document", "Text", "Diagram", "Math", "Drawing" and "Raw Content")
            contentPart = contentPackage.createPart("Text");
        } catch (IOException e) {
            Log.e(TAG, "Failed to open package \"" + packageName + "\"", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Failed to open package \"" + packageName + "\"", e);
        }

        setTitle("Type: " + contentPart.getType());

        // wait for view size initialization before setting part
        editorView.post(new Runnable() {
            @Override
            public void run() {
                editorView.getRenderer().setViewOffset(0, 0);
                editorView.getRenderer().setViewScale(1);
                editorView.setVisibility(View.VISIBLE);
                editor.setPart(contentPart);
            }
        });

    }


    @Override
    public void showResponseSelector(final boolean show, String ans) {
        this.answer = ans;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }


    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String answerTried = matches.get(0);
            System.out.println("Answer: " + answerTried);
            if(this.answer.equals(answerTried)) {
                responseView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void partChanging(Editor editor, ContentPart oldPart, ContentPart newPart) {
        // no-op
    }

    @Override
    public void partChanged(Editor editor) {
        invalidateOptionsMenu();
    }

    @Override
    public void contentChanged(Editor editor, String[] blockIds) {
        invalidateOptionsMenu();
    }

    @Override
    public void onError(Editor editor, String blockId, String message) {
        Log.e(TAG, "Failed to edit block \"" + blockId + "\"" + message);
    }

}
