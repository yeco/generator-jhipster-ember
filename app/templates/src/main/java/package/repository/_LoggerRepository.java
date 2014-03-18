package <%=packageName%>.repository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import <%=packageName%>.domain.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Repository
@SuppressWarnings("unchecked")
public class LoggerRepository implements PagingAndSortingRepository<Logger, String> {

    private LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    @Override
    public Iterable<Logger> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Logger> findAll(Pageable pageable) {
        List<Logger> loggers = new ArrayList<>();
        final Integer offset = pageable.getOffset();
        final Integer end = pageable.getOffset() + pageable.getPageSize();
        final Integer total = context.getLoggerList().size();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList().subList(offset, end > total ? total - 1 : end)) {
            loggers.add(new Logger(logger));
        }
        return new PageImpl<>(loggers, pageable, total);
    }

    @Override
    public Logger save(Logger entity) {
        final ch.qos.logback.classic.Logger logger = context.getLogger(entity.getId());
        logger.setLevel(Level.valueOf(entity.getLevel()));
        return new Logger(logger);
    }

    @Override
    public <S extends Logger> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public Logger findOne(String id) {
        final ch.qos.logback.classic.Logger logger = context.getLogger(id);

        return logger == null ? null : new Logger(logger);
    }

    @Override
    public boolean exists(String id) {
        return context.getLogger(id) != null;
    }

    @Override
    public Iterable<Logger> findAll() {
        return null;
    }

    @Override
    public Iterable<Logger> findAll(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void delete(Logger entity) {

    }

    @Override
    public void delete(Iterable<? extends Logger> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
