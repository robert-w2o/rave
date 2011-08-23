/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.RegionService;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultNewAccountService implements NewAccountService {

    protected final Logger logger=LoggerFactory.getLogger(getClass());

    private final UserService userService;
    private final PageService pageService;
    private final PageLayoutService pageLayoutService;
    private final RegionService regionService;

    @Autowired 
    private SaltSource saltSource;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultNewAccountService(UserService userService, PageService pageService, PageLayoutService pageLayoutService, RegionService regionService) {
        this.userService = userService;
        this.pageService = pageService;
        this.pageLayoutService = pageLayoutService;
        this.regionService = regionService;
    }

    @Override
    public void createNewAccount(String userName, String password, String userPageLayout) throws Exception {
        final User existingUser = userService.getUserByUsername(userName);
        if (existingUser != null) {
            throw new IllegalArgumentException("A user already exists for username " + userName);
        }
        
        User user=new User();
        user.setUsername(userName);
        //This assumes we use the username for the salt.  If not, the code below will need to change.
        //See also applicationContext-security.xml
        String saltedHashedPassword=passwordEncoder.encodePassword(password,saltSource.getSalt(user));
		  logger.debug("Salt Source: {}", saltSource.getSalt(user));
        user.setPassword(saltedHashedPassword);

        user.setExpired(false);
        user.setLocked(false);
        user.setEnabled(true);
        userService.registerNewUser(user);

        // Return the newly registered user and create a new default page for them	  
        pageService.addNewDefaultPage(userService.getUserByUsername(user.getUsername()), userPageLayout);		  
    }	 
}