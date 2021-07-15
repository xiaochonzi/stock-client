package com.zhiyun.stock.redis;

import org.springframework.data.redis.core.*;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisService {

    private RedisTemplate redisTemplate;

    private ValueOperations<String, Object> valueOperations;

    private HashOperations<String, String, Object> hashOperations;

    private ListOperations<String, Object> listOperations;

    private SetOperations<String, Object> setOperations;

    private ZSetOperations<String, Object> zSetOperations;

    private GeoOperations<String, Object> geoOperations;

    private ClusterOperations<String, Object> clusterOperations;

    public RedisService(RedisTemplate redisTemplate) {
        this.init(redisTemplate);
    }

    private void init(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
        this.setOperations = redisTemplate.opsForSet();
        this.zSetOperations = redisTemplate.opsForZSet();
        this.geoOperations = redisTemplate.opsForGeo();
        this.clusterOperations = redisTemplate.opsForCluster();
    }

    //=============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    public void del(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key) {
        return key == null ? null : (T) valueOperations.get(key);
    }

    public <T> List<T> getArray(String key) {
        return key == null ? null : (List<T>) valueOperations.get(key);
    }

    public <K, V> Map<K, V> getMap(String key) {
        return key == null ? null : (Map<K, V>) valueOperations.get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public <T> boolean set(String key, T value) {
        try {
            valueOperations.set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                valueOperations.set(key, value, time, unit);
            } else {
                return set(key, value);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return valueOperations.increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return valueOperations.increment(key, -delta);
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public <T> T hGet(String key, String item) {
        return (T) hashOperations.get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public <T> Map<String, T> hGetAll(String key) {
        return (Map<String, T>) hashOperations.entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public <T> boolean hSet(String key, Map<String, T> map) {
        try {
            hashOperations.putAll(key, map);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public <T> boolean hSet(String key, Map<String, T> map, long time, TimeUnit unit) {
        try {
            hashOperations.putAll(key, map);
            if (time > 0) {
                expire(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public <T> boolean hSet(String key, String item, T value) {
        try {
            hashOperations.put(key, item, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public <T> void hDel(String key, T... item) {
        hashOperations.delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return hashOperations.hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public long hIncr(String key, String item, long by) {
        return hashOperations.increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public long hDecr(String key, String item, long by) {
        return hashOperations.increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> sGet(String key) {
        try {
            return (Set<T>) setOperations.members(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public <T> boolean sHasKey(String key, T value) {
        try {
            return setOperations.isMember(key, value);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public <T> long sSet(String key, T... values) {
        try {
            return setOperations.add(key, values);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public <T> long sSet(String key, long time, TimeUnit unit, T... values) {
        try {
            Long count = setOperations.add(key, values);
            if (time > 0) expire(key, time, unit);
            return count;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sLength(String key) {
        try {
            return setOperations.size(key);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public <T> long sDel(String key, T... values) {
        try {
            Long count = setOperations.remove(key, values);
            return count;
        } catch (Exception e) {
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public <T> List<T> lGet(String key, long start, long end) {
        try {
            return (List<T>) listOperations.range(key, start, end);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取list缓存的所有内容
     *
     * @param key
     * @return
     */
    public <T> List<T> lGetAll(String key) {
        return lGet(key, 0, -1);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lSize(String key) {
        try {
            return listOperations.size(key);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public <T> T lIndexOf(String key, long index) {
        try {
            return (T) listOperations.index(key, index);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean lSet(String key, T value) {
        try {
            listOperations.rightPush(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T lGet(String key) {
        try {
            return (T) listOperations.leftPop(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public <T> boolean lSet(String key, T value, long time, TimeUnit unit) {
        try {
            listOperations.rightPush(key, value);
            if (time > 0) expire(key, time, unit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean lSet(String key, List<T> value) {
        try {
            listOperations.rightPushAll(key, value.toArray());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public <T> boolean lSet(String key, List<T> value, long time, TimeUnit unit) {
        try {
            listOperations.rightPushAll(key, value.toArray());
            if (time > 0) expire(key, time, unit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public <T> boolean lSetIndex(String key, long index, T value) {
        try {
            listOperations.set(key, index, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public <T> long lRemove(String key, long count, T value) {
        try {
            Long remove = listOperations.remove(key, count, value);
            return remove;
        } catch (Exception e) {
            return 0;
        }
    }


    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }


    public ValueOperations getValueOperations() {
        return valueOperations;
    }

    public HashOperations getHashOperations() {
        return hashOperations;
    }

    public ListOperations getListOperations() {
        return listOperations;
    }

    public SetOperations getSetOperations() {
        return setOperations;
    }

    public ZSetOperations getzSetOperations() {
        return zSetOperations;
    }

    public ClusterOperations<String, Object> getClusterOperations() {
        return clusterOperations;
    }

    public GeoOperations<String, Object> getGeoOperations() {
        return geoOperations;
    }
}
