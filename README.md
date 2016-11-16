---
title: SqliteOpenHelper实现复合查询
date: 2016-11-16 19:55:30
tags: ["sqlite", "数据库", "android"]
---

数据库在android是经常会使用到的东西, 但毕竟是在移动端的东西，都是一些比较轻量级的操作，无非就是增删该查，现在也有很多数据库的框架可以使用，能够很方便的简化对数据库的操作。最近在项目用到数据库，项目本身就比较小实在没有必要为了简化操作去集成一个框架进来。然而确是碰到了稍微复杂一点数据库查询，自己花了点时间去整理了一下在次记录下来。

## SqliteOpenHelper 

>SQLite，是一款轻型的数据库，是遵守ACID的关系型数据库管理系统，它包含在一个相对小的C库中。它是D.RichardHipp建立的公有领域项目。它的设计目标是嵌入式的，而且目前已经在很多嵌入式产品中使用了它，它占用资源非常的低，在嵌入式设备中，可能只需要几百K的内存就够了。它能够支持Windows/Linux/Unix等等主流的操作系统，同时能够跟很多程序语言相结合，比如 Tcl、C#、PHP、Java等，还有ODBC接口，同样比起Mysql、PostgreSQL这两款开源的世界著名数据库管理系统来讲，它的处理速度比他们都快。SQLite第一个Alpha版本诞生于2000年5月。 至2015年已经有15个年头，SQLite也迎来了一个版本 SQLite 3已经发布

## sqlite普通查询 

SqliteOpenHelper本身是个android自带的用来管理数据库的一个类，可以很方便的对数据库进行创建和管理，增删改查。通常我们查询表使用的是query()方法。

![SqliteDB.query()](http://ofhdiyvaa.bkt.clouddn.com/sqlite.png "SqliteDB.query()")

如上图所示，通过query()我们就能轻松地对数据库实现数据的分页，排序，筛选查询。比如以下代码：

```java
public List<AdMsg> getMsgListByPositionMode(int positionMode) {
        List<AdMsg> list = null;
        rd = helper.getReadableDatabase();
        if (rd.isOpen()) {
            AdMsg msg;
            list = new ArrayList<>();
            Cursor cursor = rd.query(AdvListDBInfo.TABLE_NAME_1, null, AdvListDBInfo.COLUMN_POSITION_MODE + "=?", new String[]{positionMode + ""}, null, null, null);
            while (cursor.moveToNext()) {
                msg = new AdMsg();
                msg.setAdCode(cursor.getString(1));
                msg.setAdShowType((byte) cursor.getInt(2));
                msg.setAdType(cursor.getShort(3));
                msg.setApkVersion(cursor.getString(4));
                msg.setApkVersionInt(cursor.getLong(5));
                msg.setAppPkgnameList(cursor.getString(6));
                msg.setChannelList(cursor.getString(7));
                msg.setClickLimit(cursor.getInt(8));
                msg.setDeliveryApp(cursor.getString(9));
                msg.setDeliveryArea(cursor.getString(10));
                msg.setDeliveryChannel(cursor.getString(11));
                msg.setDisplayContent(cursor.getString(12));
                msg.setDisplayEndDate(cursor.getString(13));
                msg.setDisplayEndTime(cursor.getString(14));
                msg.setDisplayStartDate(cursor.getString(15));
                msg.setDisplayStartTime(cursor.getString(16));
                msg.setDisplayTimeSeparate(cursor.getInt(17));
                msg.setDisplayTitle(cursor.getString(18));
                msg.setImageUrl(cursor.getString(19));
                msg.setLinkUrl(cursor.getString(20));
                msg.setPackageName(cursor.getString(21));
                msg.setPositionMode(cursor.getInt(22));
                msg.setSortWeight(cursor.getInt(23));
                list.add(msg);
            }
            cursor.close();
        }
        return list;
    }
```


## sqlite复合查询 
但是有很多情况这几个并不能满足我们的需求的。比如有一张User表，需要查询age都是"1"或者"2"的时候，此为情形一，或者age为"2"、firstName为"xiao"的时候，此为情形二，query就很难满足我们的需求了。于是SqliteOpenHelper提供了rawQuery()方法来帮助我解决这个问题。

![mixQuery.png](http://ofhdiyvaa.bkt.clouddn.com/mixQuery.png "mixQuery.png")

能够清楚地看到这个方法是需要我们使用sql语句作为字符串传入进行查询的。

##### 在情形一中，我们的需求是查询满足某一个条件的数据就将其取出。这里我是用sql中的"in"关键字作为查询判断。
```sql
    SELECT * FROM table WHERE selection IN ('selectionArgs1', 'selectionArgs2', 'selectionArgs3', ...);
```

具体实现代码如下：

```java
    public List<User> queryByAge(String[] ages) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<User> list = null;
        User user;
        if (db.isOpen()) {
            list = new ArrayList<>();
            String sql = "select * from " + TABLE + " where age in (";
            for (String age : ages) {
                sql = sql + "\'" + age + "\'" + ", ";
            }
            sql = sql + "\'" + ages[0] + "\'" + ")";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                user = new User();
                user.setAge(cursor.getInt(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                list.add(user);
            }
            cursor.close();
        }

        return list;
    }
```

##### 在情形二中，我们的需求是查询同时满足多个条件的数据蔡将其取出。使用了sql中的"and"关键字进行查询。

```sql
    SELECT * FROM table WHERE selection = selectionArgs1 AND selection = selectionArgs2 AND selection = selectionArgs3;
```

具体实现代码如下：

```java
    public List<User> query(HashMap<String, String> params) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<User> list = null;
        if (db.isOpen()) {
            list = new ArrayList<>();
            String sql = "select * from " + TABLE + " where ";
            for (String key : params.keySet()) {
                sql += key + " = " + "\'" + params.get(key) + "\'" + " and ";
            }
            int index = sql.lastIndexOf("and");
            sql = sql.substring(0, index);
            User user;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                user = new User();
                user.setAge(cursor.getInt(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                list.add(user);
            }
            cursor.close();
        }
        return list;
    }
```

以上代码的demo详见我[github](https://github.com/null123dzq/SqliteOpenHelperTest).
