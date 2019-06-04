package sg.edu.rp.c346.p07_smsretriever;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSecond extends Fragment {

    Button btnRetrieve;
    EditText etWord;
    TextView tvOutput;


    public FragmentSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        etWord = view.findViewById(R.id.etWord);
        btnRetrieve = view.findViewById(R.id.btnRetrieve);
        tvOutput = view.findViewById(R.id.tvOutput);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String word = etWord.getText().toString();
                String[] inter = word.trim().split(" ");

                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};

                ContentResolver cr = getActivity().getContentResolver();

                // Fetch SMS Message from Built-in Content Provider

                String smsBody = "";

                for (int i = 0; i < inter.length; i++) {

                    String filter = "body LIKE ?";
                    String[] filterArgs = {"%" + etWord.getText().toString() + "%"};

                    Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);

                    if (cursor.moveToFirst()) {
                        do {
                            long dateInMillis = cursor.getLong(0);
                            String date = (String) DateFormat
                                    .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                            String address = cursor.getString(1);
                            String body = cursor.getString(2);
                            String type = cursor.getString(3);
                            if (type.equalsIgnoreCase("1")) {
                                type = "Inbox:";
                            } else {
                                type = "Sent:";
                            }
                            smsBody += type + " " + address + "\n at " + date
                                    + "\n\"" + body + "\"\n\n";
                        } while (cursor.moveToNext());
                    }
                    tvOutput.setText(smsBody);
                }
            }

        });

        return view;
    }

}
