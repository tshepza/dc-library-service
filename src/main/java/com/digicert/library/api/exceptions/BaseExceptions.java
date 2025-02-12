package com.digicert.library.api.exceptions;

import java.io.Serializable;

public class BaseExceptions extends RuntimeException implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String metaData;

        public BaseExceptions(String msg, String metadata) {
            super(msg);
            this.metaData = metadata;
        }

        public String getMetaData() {
            return metaData;
        }
}
