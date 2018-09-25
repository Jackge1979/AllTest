package com.esper.introduction.myself;

/**
 * Created by lcc on 2018/9/25.
 */
public class MakeData {

    public static void main(String[] args) throws  Exception {
        int s = 0;
        for (int i=0;i< 10;i++){
            long timeStamp = System.currentTimeMillis()/1000+s;
            System.out.println(10+","+timeStamp);
            s++;
        }

        for (int i=0;i< 10;i++){
            long timeStamp = System.currentTimeMillis()/1000+s;
            int count = 10+i;
            System.out.println(count+","+timeStamp);
            s++;
        }

        for (int i=0;i< 10;i++){
            long timeStamp = System.currentTimeMillis()/1000+s;
            System.out.println(10+","+timeStamp);
            s++;
        }

    }
}

/**

 10,1537867121
 10,1537867122
 10,1537867123
 10,1537867124
 10,1537867125
 10,1537867126
 10,1537867127
 10,1537867128
 10,1537867129
 10,1537867130
 10,1537867131
 11,1537867132
 12,1537867133
 13,1537867134
 14,1537867135
 15,1537867136
 16,1537867137
 17,1537867138
 18,1537867139
 19,1537867140
 10,1537867141
 10,1537867142
 10,1537867143
 10,1537867144
 10,1537867145
 10,1537867146
 10,1537867147
 10,1537867148
 10,1537867149
 10,1537867150


 */