package mx.com.pineahat.auth10;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Stephani on 05/07/2015.
 */
public class MyFragment extends Fragment{
    public final static String ID_GRUPO = "key_text";

    private String mText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mText = getArguments().getString(ID_GRUPO);

        View v = inflater.inflate(R.layout.fragment, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tv_fragment);
        tv.setText(mText);

        return v;
    }
}
