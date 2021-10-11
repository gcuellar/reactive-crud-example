package es.gcuellar.reactive.crud.server.utils;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.FieldDefinitionBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import io.github.benas.randombeans.randomizers.time.OffsetDateTimeRandomizer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.benas.randombeans.FieldDefinitionBuilder.field;

public class ObjectRandomizer {

    private static final int DEFAULT_MAX_COLLECTIONS_SIZE = 10;

    private ObjectRandomizer() {
        throw new UnsupportedOperationException("Cannot instantiate utilities class");
    }

    public static Integer randomInteger(RandomGenerationExclusion... exclusions) {
        return random(Integer.class , DEFAULT_MAX_COLLECTIONS_SIZE, exclusions);
    }

    public static Long randomLong(RandomGenerationExclusion... exclusions) {
        return random(Long.class , DEFAULT_MAX_COLLECTIONS_SIZE, exclusions);
    }

    public static String randomString(RandomGenerationExclusion... exclusions) {
        return random(String.class , DEFAULT_MAX_COLLECTIONS_SIZE, exclusions);
    }

    public static <T> T random(Class<T> classType, RandomGenerationExclusion... exclusions) {
        return random(classType, DEFAULT_MAX_COLLECTIONS_SIZE, exclusions);
    }

    public static <T> T random(Class<T> classType, int maxCollectionsSize, RandomGenerationExclusion... exclusions) {
        return buildRandomizer(maxCollectionsSize, exclusions).nextObject(classType);
    }

    public static <T> List<T> randomListOf(Class<T> classType, int amount, RandomGenerationExclusion... exclusions) {
        return randomListOf(classType, amount, DEFAULT_MAX_COLLECTIONS_SIZE, exclusions);
    }

    public static <T> List<T> randomListOf(Class<T> classType, int amount, int maxCollectionsSize, RandomGenerationExclusion... exclusions) {
        return buildRandomizer(maxCollectionsSize, exclusions).objects(classType, amount).collect(Collectors.toList());
    }

    public static RandomGenerationExclusion exclude(String fieldName, Class<?> type, Class<?> inClass) {
        return new RandomGenerationExclusion(fieldName, type, inClass);
    }

    private static EnhancedRandom buildRandomizer(int maxCollectionsSize, RandomGenerationExclusion... exclusions) {
        return builder(maxCollectionsSize, exclusions).build();
    }

    private static EnhancedRandomBuilder builder(int maxCollectionsSize, RandomGenerationExclusion... exclusions) {
        EnhancedRandomBuilder builder = new EnhancedRandomBuilder()
                .collectionSizeRange(1, maxCollectionsSize)
                .overrideDefaultInitialization(true);
        builder.randomize(ZonedDateTime.class, (Randomizer<ZonedDateTime>) () -> new OffsetDateTimeRandomizer().getRandomValue().toZonedDateTime());

        Stream.of(exclusions).forEach(exclusion -> {
            FieldDefinitionBuilder field = field();
            if (exclusion.fieldName != null) {
                field.named(exclusion.fieldName);
            }
            if (exclusion.type != null) {
                field.ofType(exclusion.type);
            }
            if (exclusion.inClass != null) {
                field.inClass(exclusion.inClass);
            }
            builder.exclude(field.get());
        });
        return builder;
    }

    public static class RandomGenerationExclusion {
        private final String fieldName;
        private final Class<?> type;
        private final Class<?> inClass;

        public RandomGenerationExclusion(String fieldName, Class<?> type, Class<?> inClass) {
            this.fieldName = fieldName;
            this.type = type;
            this.inClass = inClass;
        }
    }
}
