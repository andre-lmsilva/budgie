package io.geekmind.budgie.model.mapper;

public interface Mapper<S, T> {

    T mapTo(S source);

    S mapFrom(T target);

}
