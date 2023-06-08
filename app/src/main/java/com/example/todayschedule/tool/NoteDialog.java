package com.example.todayschedule.tool;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.R;
import com.example.todayschedule.bean.Note;
import com.xujiaji.happybubble.BubbleDialog;

/**
 * @ClassName NoteDialog
 * @Description TODO
 * @Author 找小样
 * @DATE 2023/6/8 21:28
 * @Version 1.0
 */
public class NoteDialog extends BubbleDialog{

    private ImageView delButton;
    private TextView textView;
    private View.OnClickListener delListener;

    public NoteDialog(Context context) {
        super(context);
        View rootView = LayoutInflater.from(context).inflate(R.layout.bubble_view, null);
        addContentView(rootView);
        textView = rootView.findViewById(R.id.textView4);
        delButton = rootView.findViewById(R.id.note_del_button);
    }

    public NoteDialog setNote(Note note){
        textView.setText(Html.fromHtml(note.getContent()));
        return this;
    }

    public void setDelListener(View.OnClickListener delListener) {
        this.delListener = delListener;
        delButton.setOnClickListener(delListener);
    }
}
