package com.grouter;

import java.util.HashMap;

/**
 * 用于多工程项目
 */
class DefaultMultiProjectGRouter extends GRouter {
    DefaultMultiProjectGRouter() {
        super("", "", new HashMap<String, String>(), new HashMap<String, String>(),new HashMap<String, String>());
    }
}
