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
package org.apache.rave.portal.model;

import org.apache.rave.persistence.BasicEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Represents a person in the persistence context
 */
@Entity
@Table(name = "raveuser")
@NamedQueries(value = {
    @NamedQuery(name = Person.FIND_BY_USERNAME, query = "select p from Person p where p.username like :username"),
    @NamedQuery(name = Person.FIND_FRIENDS_BY_USERNAME, query = "select a.followed from PersonAssociation a where a.follower.username = :username"),
    @NamedQuery(name = Person.FIND_BY_GROUP_MEMBERSHIP, query = "select m from Group g join g.members m where exists " +
            "(select 'found' from g.members b where b.username = :username) and m.username <> :username")
})
public class Person implements BasicEntity {

    public static final String FIND_BY_USERNAME = "Person.findByUsername";
    public static final String FIND_FRIENDS_BY_USERNAME = "Person.findFriendsByUsername";
    public static final String FIND_BY_GROUP_MEMBERSHIP = "Person.findByGroupMembership";
    public static final String USERNAME_PARAM = "username";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "personIdGenerator")
    @TableGenerator(name = "personIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "raveuser", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "username", unique = true)
    private String username;

    @Basic
    @Column(name = "email", unique = true)
    private String email;

    @Basic
    @Column(name = "display_name")
    private String displayName;

    @Basic
    @Column(name = "additional_name", length = 255)
    private String additionalName;

    @Basic
    @Column(name = "family_name", length = 255)
    private String familyName;

    @Basic
    @Column(name = "given_name", length = 255)
    private String givenName;

    @Basic
    @Column(name = "honorific_prefix", length = 255)
    private String honorificPrefix;

    @Basic
    @Column(name = "honorific_suffix", length = 255)
    private String honorificSuffix;

    @Basic
    @Column(name = "preferred_name")
    private String preferredName;

    @Basic
    @Column(name = "about_me")
    private String aboutMe;

    @Basic
    @Column(name = "status")
    private String status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "person_address_join",
            joinColumns = @JoinColumn(name = "address_id", referencedColumnName = "entity_id"),
            inverseJoinColumns = @JoinColumn(name="person_id", referencedColumnName = "entity_id"))
    private List<Address> addresses;

    @OneToMany(targetEntity = PersonProperty.class)
    @JoinColumn(name = "person_id", referencedColumnName = "entity_id")
    private List<PersonProperty> properties;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "person_friends_jn",
            joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "entity_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id", referencedColumnName = "entity_id"))
    private List<Person> friends;

    @Transient
    private Map<String, List<PersonProperty>> mappedProperties;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getHonorificPrefix() {
        return honorificPrefix;
    }

    public void setHonorificPrefix(String honorificPrefix) {
        this.honorificPrefix = honorificPrefix;
    }

    public String getHonorificSuffix() {
        return honorificSuffix;
    }

    public void setHonorificSuffix(String honorificSuffix) {
        this.honorificSuffix = honorificSuffix;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<PersonProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<PersonProperty> properties) {
        this.properties = properties;
    }

    public List<Person> getFriends() {
        return friends;
    }

    public void setFriends(List<Person> friends) {
        this.friends = friends;
    }

    public Map<String, List<PersonProperty>> getMappedProperties() {
        return mappedProperties;
    }

    public void setMappedProperties(Map<String, List<PersonProperty>> mappedProperties) {
        this.mappedProperties = mappedProperties;
    }
}
