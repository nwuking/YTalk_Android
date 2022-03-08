package com.nwuking.ytalk;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class FavoriteAdapter extends BaseAdapter {
    private List<FavoriteItem> shouMessages;
    private Context context;
    private DbUtils db;
    private String drawablename;
    private Date date;
    Bitmap bm;

    public FavoriteAdapter(Context context, List<FavoriteItem> messages) {
        super();
        this.context = context;
        this.shouMessages = messages;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return shouMessages.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return shouMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        FavoriteItem message = shouMessages.get(position);
        convertView = LayoutInflater.from(context).inflate(
                R.layout.item_shoucang, null);

        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.name = (TextView) convertView.findViewById(R.id.tv_window_title);
        holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
        holder.head = (ImageView) convertView.findViewById(R.id.img_head);
        holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
        holder.time.setText(message.getTime());
        holder.name.setText(message.getName());
        String idd = message.getMsgtext();
        String[] ss = idd.split("\\|");
        String headpath = message.getHeadpath();
        if (message.getHeadpath().equals("")) {
            // holder.head.setBackground(background);
        } else {

            bm = PictureUtil
                    .decodePicFromFullPath("/sdcard/com.nwuking.ytalk/" + headpath);

            holder.head.setImageBitmap(bm);

        }

        if (message.getType().equals("2")) {
            holder.tv_text.setVisibility(View.GONE);
            holder.iv_image.setVisibility(View.VISIBLE);
            String a = idd.substring(2, idd.length() - 2);
            String[] sss = a.split("\\,");
            String name = sss[0];
            name = name.replaceAll("\"", "");
            bm = PictureUtil.decodePicFromFullPath("/sdcard/com.nwuking.ytalk/" + name);
            holder.iv_image.setImageBitmap(bm);

        } else {
            holder.tv_text.setVisibility(View.VISIBLE);
            holder.iv_image.setVisibility(View.GONE);

            for (int i = 0; i < ss.length; i++) {
                String id = ss[i];
                if (id.startsWith("[") && id.endsWith("]")) {
                    String faceId = id.substring(1, id.length() - 1);
                    db = DbUtils.create(context);
                    try {
                        List<Face> list = db.findAll(Selector.from(Face.class)
                                .where("faceid", "=", faceId));
                        if (list != null) {
                            for (int f = 0; f < list.size(); f++) {
                                String fid = list.get(f).getFaceid();
                                if (fid.equals(faceId)) {
                                    drawablename = list.get(f).getFile();

                                }
                            }
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    String html = "<img src='" + drawablename + "'/>";
                    Html.ImageGetter imgGetter = new Html.ImageGetter() {

                        @Override
                        public Drawable getDrawable(String source) {
                            Bitmap b = getImageFromAssetsFile(source);
                            Drawable d = new BitmapDrawable(b);
                            d.setBounds(0, 0, 60, 60);
                            return d;
                        }
                    };
                    CharSequence charSequence = Html.fromHtml(html, imgGetter,
                            null);

                    holder.tv_text.append(charSequence);
                } else {
                    holder.tv_text.append(id);
                }
            }

        }

        return convertView;
    }

    @Override
    public int getContentView() {
        return 0;
    }

    @Override
    public void onInitView(View view, int position) {

    }

    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    static class ViewHolder {
        TextView name, time, tv_text;
        ImageView iv_image, head;
    }
}
