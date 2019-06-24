package io.geekmind.budgie.model.mapper;

/**
 * Defines a common interface to be implemented by all class that perform some kind of mapping operation between
 * entities and DTOs.
 * @param <S>   Source type, which instances will be mapped to the target type.
 * @param <T>   Target type returned by the mapping operation.
 */
@FunctionalInterface
public interface Mapper<S, T> {

    /**
     * Maps the attribute values from the received source instance to the attributes of a new instance of the target
     * type.
     * @param source    Source instance to have its attributes values mapped to the attributes of a new instance of the
     *                  target type.
     * @return New instance of the target type filled in with the values of the received source instance.
     */
    T mapTo(S source);

}
