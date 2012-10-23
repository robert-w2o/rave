/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository;

import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 */
public interface MongoModelOperations<T> {
    long count(Query query);
    T findOne(Query query);
    List<T> find(Query query);
    T get(long id);
    T save(T item);
    void remove(Query query);
    int update(Query query, Update update);
    <E> MapReduceResults<E> mapReduce(String mapFunction, String reduceFunction, Class<E> entityClass);
    <E> MapReduceResults<E> mapReduce(String collection, String mapFunction, String reduceFunction, Class<E> entityClass);
    <E> MapReduceResults<E> mapReduce(Query query, String mapFunction, String reduceFunction, Class<E> entityClass);
    <E> MapReduceResults<E> mapReduce(String collection, Query query, String mapFunction, String reduceFunction, Class<E> entityClass);
}
