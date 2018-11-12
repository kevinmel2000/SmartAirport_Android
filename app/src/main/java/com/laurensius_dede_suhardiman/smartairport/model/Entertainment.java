package com.laurensius_dede_suhardiman.smartairport.model;

public class Entertainment {
    String id;
    String video_title;
    String description;
    String yt_id;

    public Entertainment(String id,
            String video_title,
            String description,
            String yt_id
    ){
        this.id = id;
        this.video_title = video_title;
        this.description = description;
        this.yt_id = yt_id;
    }

    public String getId() {
        return id;
    }

    public String getVideo_title() {
        return video_title;
    }

    public String getDescription() {
        return description;
    }

    public String getYt_id() {
        return yt_id;
    }
}
