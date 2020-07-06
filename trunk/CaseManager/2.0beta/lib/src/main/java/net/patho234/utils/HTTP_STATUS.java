/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

/**
 *
 * @author rehkind
 */
public class HTTP_STATUS {
    public static final int OK=200;

    public static final int BAD_REQUEST=400;
    public static final int NOT_FOUND=404;
    public static final int CONSTRAINTS_VIOLATED=422;
    public static final int INTERNAL_SERVER_ERROR=500;
    // custom stati:
    public static final int NOT_CACHED=-345;
    public static final int CACHED=-200;
}
