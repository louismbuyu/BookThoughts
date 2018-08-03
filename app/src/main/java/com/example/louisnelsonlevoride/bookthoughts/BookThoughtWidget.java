package com.example.louisnelsonlevoride.bookthoughts;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.louisnelsonlevoride.bookthoughts.Onboarding.MainActivity;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;

import static android.support.constraint.Constraints.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class BookThoughtWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
        String widgetText = CurrentUserSingleton.getInstance().getFavouriteBookTitle(context);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.book_thought_widget);
        if (widgetText != null){
            if (!widgetText.isEmpty()){

                views.setTextViewText(R.id.appwidget_text, widgetText);
                String bookImageUrl = CurrentUserSingleton.getInstance().getFavouriteBookImageUrl(context);
                Glide.with(context).asBitmap().load(bookImageUrl).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        views.setImageViewBitmap(R.id.appwidget_image,resource);

                        Intent intent = new Intent(context, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
                        views.setOnClickPendingIntent(R.id.appwidget_image,pendingIntent);

                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                });
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

