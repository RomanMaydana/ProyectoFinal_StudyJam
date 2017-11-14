package com.maydana.roman.miepisodio;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.content.Intent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;


public class EpisodioAdapter extends RecyclerSwipeAdapter<EpisodioAdapter.EpsodioViewHolder> {
    private List<Episodio> dataset;
    private Context context;
    private onEpisodioSelectListener onEpisodioSelectListener;

    public EpisodioAdapter(Context context, EpisodioAdapter.onEpisodioSelectListener onEpisodioSelectListener) {
        this.context = context;
        this.onEpisodioSelectListener = onEpisodioSelectListener;
        dataset = new ArrayList<>();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public interface onEpisodioSelectListener {
        void onEpisodioSelected(Episodio episodio);
        void onEpisodioLongSelected(Episodio episodio,int pos);
    }

    @Override
    public EpsodioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episodio, parent, false);
        return new EpsodioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EpsodioViewHolder holder, final int position) {
        final Episodio episodio = dataset.get(position);
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(holder.swipeLayout);
                dataset.remove(position);
                notifyDataSetChanged();
                MainActivity.sqLiteHelper.deleteData(episodio.getId());
                mItemManger.closeAllItems();
                Toast.makeText(context, context.getString(R.string.eliminado) + episodio.getTitulo().toString() + "!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.titulo.setText(episodio.getTitulo());
        String ruta = episodio.getRutaImagen();
        File imageFile = new File(ruta);
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        holder.imagen.setImageBitmap(redimensionarImagenMaximo(bitmap, 400, 350));
        mItemManger.bindView(holder.itemView, position);

        holder.setOnEpisodioSelectListener(episodio, onEpisodioSelectListener);
        holder.setOnEpisodioLongSelectListener(episodio,onEpisodioSelectListener,position);
    }

    private Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, int newHeigth) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    @Override
    public void onViewAttachedToWindow(EpsodioViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        animateCircularReveal(holder.itemView);
    }

    private void animateCircularReveal(View view) {
        int centerX = 0;
        int centerY = 0;
        int starRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, starRadius, endRadius);
        view.setVisibility(View.VISIBLE);
        animation.start();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
    public void setItemData(Episodio episodio,int pos){
        dataset.set(pos,episodio);
        notifyDataSetChanged();
    }
    public  void addItemData(Episodio episodio){
        dataset.add(episodio);
        notifyDataSetChanged();
    }
    public void setData(List<Episodio> list) {
        if (list == null) {
            dataset = new ArrayList<>();
        } else {
            dataset = list;
        }
        notifyDataSetChanged();
    }

    public class EpsodioViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        ImageView imagen;
        TextView titulo;
        SwipeLayout swipeLayout;
        Button buttonDelete;

        public EpsodioViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_episodio);
            swipeLayout = itemView.findViewById(R.id.swipe);
            imagen = itemView.findViewById(R.id.imagen_card);
            titulo = itemView.findViewById(R.id.titulo_card);
            buttonDelete = itemView.findViewById(R.id.delete);

        }

        public void setOnEpisodioSelectListener(final Episodio episodio, final onEpisodioSelectListener onEpisodioSelectListener) {
            swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEpisodioSelectListener.onEpisodioSelected(episodio);
                }
            });
        }
        public void setOnEpisodioLongSelectListener(final Episodio episodio, final onEpisodioSelectListener onEpisodioSelectListener,final int pos){
            swipeLayout.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onEpisodioSelectListener.onEpisodioLongSelected(episodio,pos);
                    return false;
                }
            });
        }


    }
}
