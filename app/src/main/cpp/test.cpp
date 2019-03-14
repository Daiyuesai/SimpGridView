//
// Created by daiyuesai on 2019/3/12.
//

#include "test.h"


test::test(int a, int b) {
    w =a ;
    h = b;
}

int test::getWidth() {
    return w;
}

int test::getHeight() {
    return h;
}

int test::getArea() {
    return w*h;
}
