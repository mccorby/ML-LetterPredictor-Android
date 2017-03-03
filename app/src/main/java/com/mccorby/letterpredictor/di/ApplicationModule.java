package com.mccorby.letterpredictor.di;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.mccorby.letterpredictor.BuildConfig;
import com.mccorby.letterpredictor.domain.SharedConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context) {

        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public AssetManager provideAssetManager(Context context) {
        return context.getAssets();
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    @Named("configName")
    public String provideConfigFileName() {
        return BuildConfig.CONFIG_FILENAME;
    }

    @Singleton
    @Provides
    public SharedConfig provideSharedConfig(@Named("configName") String fileName, Gson gson, AssetManager assetManager) {
        // TODO This should be done by an object
        BufferedReader reader = null;
        SharedConfig sharedConfig = new SharedConfig();
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            sharedConfig = gson.fromJson(reader, SharedConfig.class);
        } catch (IOException e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return sharedConfig;
    }

}
