package android.interview.drama.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DramaList implements Serializable {

    @SerializedName("data")
    public List<Drama> list;

    public static class Drama implements Serializable {
        /**
         * drama_id : 1
         * name : 致我們單純的小美好
         * total_views : 23562274
         * created_at : 2017-11-23T02:04:39.000Z
         * thumb : https://i.pinimg.com/originals/61/d4/be/61d4be8bfc29ab2b6d5cab02f72e8e3b.jpg
         * rating : 4.4526
         */

        public int drama_id;
        public String name;
        public int total_views;
        public String created_at;
        public String thumb;
        public Double rating;
    }

}
