package proxy;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.memory.impl.LRUMemoryImpl;
import com.safframework.rxcache.proxy.CacheProvider;
import domain.User;

/**
 * Created by tony on 2018/10/31.
 */
public class TestCacheProvider {

    public static void main(String[] args) {


        RxCache.config(new RxCache.Builder().memory(new LRUMemoryImpl(512)));

        RxCache rxCache = RxCache.getRxCache();

        CacheProvider cacheProvider = new CacheProvider.Builder().rxCache(rxCache).build();

        Provider provider = cacheProvider.create(Provider.class);

        User u = new User();
        u.name = "tony";
        u.password = "123456";

        provider.putData(u);

//        Record<User> record = provider.getData(User.class);
//
//        if (record==null) {
//
//            System.out.println("record is null");
//        } else {
//
//            System.out.println(record.getData().name);
//        };

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Record<User> record = rxCache.get("user",User.class);

        if (record==null) {

            System.out.println("record is null");
        }
    }
}