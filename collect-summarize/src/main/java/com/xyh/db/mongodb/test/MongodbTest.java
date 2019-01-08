package com.xyh.db.mongodb.test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongodbTest {

    public  static  void main(String[] args){

        test01();
    }



    public static void test01(){
        MongoClient client=new MongoClient("39.106.213.40" , 27017);//创建连接对象
        MongoDatabase database = client.getDatabase("test");//获取数据库
        MongoCollection<Document> collection = database.getCollection("test");//获取集合

        FindIterable<Document> list = collection.find();//获取文档集合
        for( Document doc: list){//遍历集合中的文档输出数据

            System.out.println("name:"+ doc.getString("name") );
            System.out.println("sex:"+ doc.getString("sex") );
            System.out.println("age:"+ doc.getDouble("age") );//默认为浮点型
            System.out.println("address:"+ doc.getString("address") );
            System.out.println("--------------------------");
        }
    }


}
