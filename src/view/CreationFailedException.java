/*
 * File: ComponentCreationFailedException.java
 * Author: Fredrik Johansson
 * Date: 2016-12-28
 */
package view;

/**
 * Will be thrown when a component failed to be created, often because
 * of failed loading of needed images
 */
class CreationFailedException extends Exception {
        public CreationFailedException() {
            super();
        }

        public CreationFailedException(String message) {
            super(message);
        }

        public CreationFailedException(String message, Throwable cause) {
            super(message, cause);
        }

        public CreationFailedException(Throwable cause) {
            super(cause);
        }
    }
