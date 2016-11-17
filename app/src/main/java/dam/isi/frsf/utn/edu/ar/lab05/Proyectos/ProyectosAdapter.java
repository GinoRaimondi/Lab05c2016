package dam.isi.frsf.utn.edu.ar.lab05.Proyectos;

import android.annotation.TargetApi;
import android.content.Context;
//import android.icu.util.Calendar;
//import android.icu.util.GregorianCalendar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.R;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

/**
 * Created by RAIMONDI on 29/09/2016.
 */

public class ProyectosAdapter extends ArrayAdapter<Proyecto> {

    private LayoutInflater inflater;

    public ProyectosAdapter(Context context, List<Proyecto> proyectos) {
        super(context, R.layout.fila_proyecto, proyectos);
        inflater = LayoutInflater.from(context);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View row = convertView;

        if (row == null) row = inflater.inflate(R.layout.fila_proyecto, parent, false);

        TextView nombre = (TextView) row.findViewById(R.id.nombre_proyecto);

        TextView id = (TextView) row.findViewById(R.id.id_proyecto);

        nombre.setText(this.getItem(position).getNombre().toString());

        id.setText(this.getItem(position).getId().toString());

        Integer color = Color.parseColor("#2196F3");

        nombre.setTextColor(color);

        id.setTextColor(color);

        SpannableString spanString = new SpannableString(nombre.getText().toString());

        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);

        nombre.setText(spanString);

        SpannableString spanString1 = new SpannableString(id.getText().toString());

        spanString1.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString1.length(), 0);

        id.setText(spanString1);

        return row;
    }
}
