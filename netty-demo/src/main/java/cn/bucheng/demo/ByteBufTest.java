package cn.bucheng.demo;
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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.Test;

/**
 * @author yinchong
 * @create 2020/1/11 14:54
 * @description ByteBuf分配及使用学习
 * <p>
 * 内存的级别有哪些？
 * <p>
 * 如何减少多线程内存分配之间的竞争？
 * <p>
 * 不同大小的内存是如何进行分配的？
 * <p>
 * ==================
 * <p>
 * 内存和内存管理器的抽象
 * <p>
 * 不同规则大小和不同类别的内存的分配策略
 * <p>
 * 内存回收过程
 * <p>
 * ==============
 * <p>
 * ByteBuf结构
 * 成员变量
 * readIndex, writeIndex   ,  capacity , maxCapacity
 * 方法：
 * read
 * write
 * set 不会移动任何指针
 * mark 操作前，把相应的指针保存起来
 * reset 回复之间的操作
 * reableBytes = writeIndex-readIndex
 * writeableBytes = capacity - writeIndex
 * <p>
 * ====================
 * ByteBuf分类
 * ByteBuf==>AbstractByteBuf[_get/_set]==>(Pooled和Unpooled/Unsafe和非Unsafe/Heap和Direct)
 * <p>
 * =================
 * ByteBufAllocator ==>AbstractByteBufAllocator==>(PoolByteBufAllocator/UnPoolByteBufAllocator)
 * 方法：
 * buffer
 * ioBuffer
 * heapBuffer
 * directBuffer
 * compositeBuffer
 * <p>
 * newDirectBuffer
 * newHeapBuffer
 * <p>
 * ==============
 * PooledByteBufAllo
 * 方法newDirectBuffer：
 * 拿到线程局部缓存 threadCache.get() PoolThreadCache(heapArena directArena )
 * 在线程局部缓存的Area上进行分配 cache.directArena
 * tinySubPageDirectCaches
 * smallSubPageDirectCaches
 * normalSubPageDirectCaches
 */
public class ByteBufTest {

    @Test
    public void unPooledTest() {
        UnpooledByteBufAllocator allocator = UnpooledByteBufAllocator.DEFAULT;
        ByteBuf byteBuf = allocator.heapBuffer(1024);
        byteBuf.release();
        System.out.println("test2");
    }

    @Test
    public void pooledTest() {
        PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
        ByteBuf byteBuf = allocator.directBuffer(1024 * 8);
        byteBuf.release();
        byteBuf = allocator.directBuffer(1024 * 8);
        byteBuf.release();
        System.out.println("hello");
    }
}
