package net.ynov.createnuclear.multiblock;

import lib.multiblock.test.impl.IMultiBlockPattern;

public record CNBlockPattern<T>(String id, T data, IMultiBlockPattern structure) {}
