package cn.bucheng.flink.trace;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import redis.clients.jedis.Jedis;

/**
 * @author yinchong
 * @create 2020/1/31 9:40
 * @description
 */
public class RedisBloomFilter {
    private static final int DEFAULT_SIZE = 2 << 24;
    private static final int[] seeds = new int[]{7, 11, 13, 31, 37, 61,};
    private Jedis jedis;
    private SimpleHash[] hashFunc = new SimpleHash[seeds.length];
    private String TRACE_BLOOM_FILTER_KEY = "trace_bloom_filter";

    private RedisBloomFilter(String redisIp, Integer port) {
        for (int i = 0; i < seeds.length; i++) {
            hashFunc[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
        jedis = new Jedis(redisIp, port);
    }

    public boolean add(String value) {
        if (contains(value)) {
            System.out.println("exist:" + value);
            return false;
        }
        for (SimpleHash f : hashFunc) {
            jedis.setbit(TRACE_BLOOM_FILTER_KEY, f.hash(value), true);
        }
        return true;
    }

    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        for (SimpleHash f : hashFunc) {
            ret = ret && jedis.getbit(TRACE_BLOOM_FILTER_KEY, f.hash(value));
        }
        return ret;
    }

    public static class SimpleHash {

        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }

    }

    private static RedisBloomFilter instance;

    public static RedisBloomFilter getInstance(String ip, int port) {
        if (instance != null) {
            return instance;
        }
        synchronized (RedisBloomFilter.class) {
            if (instance == null) {
                instance = new RedisBloomFilter(ip, port);
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        String value = "stone2083@yahoo.cn";
        RedisBloomFilter filter = RedisBloomFilter.getInstance("127.0.0.1", 6379);
        System.out.println(filter.contains(value));
        filter.add(value);
        System.out.println(filter.contains(value));
    }
}