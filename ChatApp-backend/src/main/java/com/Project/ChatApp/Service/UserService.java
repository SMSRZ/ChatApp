package com.Project.ChatApp.Service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
@Service
public class UserService {
    private final Set<String> users = Collections.synchronizedSet(new HashSet<>());

    public void addUser(String userName){
        users.add(userName);
    }

    public void removeUser(String userName){
        users.remove(userName);
    }

    public Set<String> getUsers(){
        return users;
    }
}
