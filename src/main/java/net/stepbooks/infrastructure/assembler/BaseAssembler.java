package net.stepbooks.infrastructure.assembler;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseAssembler {

    public static <T, S> T convert(S source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper.map(source, targetClazz);
    }

    public static <T, S> List<T> convert(List<S> source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return source.stream().flatMap((o) -> Stream.of(modelMapper.map(o, targetClazz)))
                .collect(Collectors.toList());
    }

}
