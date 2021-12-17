package insideworld.engine.integration.tests.entity;

import insideworld.engine.database.AbstractCrudGenericStorage;
import javax.inject.Singleton;

@Singleton
public class TwoStorage extends AbstractCrudGenericStorage<JpaTestTwo, JpaTestTwo> {

}
