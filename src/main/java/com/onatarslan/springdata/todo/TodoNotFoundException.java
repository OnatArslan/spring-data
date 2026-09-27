package com.onatarslan.springdata.todo;

import java.util.UUID;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(UUID todoID) {
        super("Todo not found with id: " + todoID);
    }

}
