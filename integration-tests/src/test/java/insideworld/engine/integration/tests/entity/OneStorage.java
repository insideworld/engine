package insideworld.engine.integration.tests.entity;

import insideworld.engine.database.AbstractCrudGenericStorage;
import insideworld.engine.database.AbstractCrudStorage;
import javax.inject.Singleton;

@Singleton
public class OneStorage extends AbstractCrudGenericStorage<JpaTestOne, JpaTestOne> {

}
