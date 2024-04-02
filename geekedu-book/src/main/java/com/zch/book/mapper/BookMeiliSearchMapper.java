package com.zch.book.mapper;

import com.zch.book.domain.dto.BookMSDTO;
import com.zch.common.meilisearch.reposotory.impl.MeiliSearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Repository
public class BookMeiliSearchMapper extends MeiliSearchRepository<BookMSDTO> {
}
