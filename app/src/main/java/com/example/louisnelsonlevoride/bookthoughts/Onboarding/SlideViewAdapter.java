package com.example.louisnelsonlevoride.bookthoughts.Onboarding;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.louisnelsonlevoride.bookthoughts.R;

public class SlideViewAdapter extends PagerAdapter {

    private Context context;
    private MainActivity mainActivity;
    LayoutInflater inflater;

    public int[] images = {
            R.drawable.onboard_save,
            R.drawable.onboard_share,
            R.drawable.onboard_discover
    };

    /*public String[] titles = {
            "SAVE QUOTES",
            "SHARE THOUGHTS",
            "DISCOVER BOOKS"
    };

    public String[] descrs = {
            "Save your favourite quotes and add thoughts to them",
            "Share your favourite quotes and thoughts with friends",
            "Discover and find your favourite reading books."

    };*/

    public SlideViewAdapter(Context context, MainActivity mainActivity) {
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_view_layout,container,false);
        ImageView imageView = view.findViewById(R.id.mainImage);
        TextView titleTV = view.findViewById(R.id.mainText);
        String title_1 = context.getString(R.string.save_quotes);
        String title_2 = context.getString(R.string.share_thoughts);
        String title_3 = context.getString(R.string.discover_books);
        String[] titles = {
                title_1,
                title_2,
                title_3
        };
        String descr1 = context.getString(R.string.descr1);
        String descr2 = context.getString(R.string.descr2);
        String descr3 = context.getString(R.string.descr3);
        String[] descrs = {
                descr1,
                descr2,
                descr3

        };
        titleTV.setText(titles[position]);
        TextView descrTV = view.findViewById(R.id.descriptionText);
        descrTV.setText(descrs[position]);
        //imageView.setImageResource(images[position]);
        Glide.with(context).load(images[position]).into(imageView);
        int temp = mainActivity.viewPager.getCurrentItem();
        container.addView(view);
        return view;
    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimary });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout)object);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        int mmm = mainActivity.viewPager.getCurrentItem();

        if (mmm == 2){
            Button button = mainActivity.findViewById(R.id.next_id);
            button.setText(R.string.join_now);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.enterApp();
                }
            });
        }else {
            Button button = mainActivity.findViewById(R.id.next_id);
            button.setText(R.string.next);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.nextAction();
                }
            });
        }
        return (view == (RelativeLayout)object);
    }
}
