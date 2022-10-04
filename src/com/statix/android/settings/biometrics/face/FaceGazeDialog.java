package com.statix.android.settings.biometrics.face;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.window.R;

public class FaceGazeDialog extends DialogFragment {
    private DialogInterface.OnClickListener mButtonListener;

    private static FaceGazeDialog newInstance() {
        return new FaceGazeDialog();
    }

    public void setButtonListener(DialogInterface.OnClickListener onClickListener) {
        this.mButtonListener = onClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return FaceEnrollDialogFactory.newBuilder(getActivity()).setTitle(R.string.face_enrolling_gaze_dialog_title).setMessage(R.string.face_enrolling_gaze_dialog_message).setPositiveButton(R.string.face_enrolling_gaze_dialog_continue, new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                FaceGazeDialog.this.lambda$onCreateDialog$0(dialogInterface, i);
            }
        }).setNegativeButton(R.string.face_enrolling_gaze_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                FaceGazeDialog.this.lambda$onCreateDialog$1(dialogInterface, i);
            }
        }).build();
    }

    private /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        this.mButtonListener.onClick(dialogInterface, i);
    }

    private /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        this.mButtonListener.onClick(dialogInterface, i);
    }
}
