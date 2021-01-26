package com.yunhwan.springbootcommunityweb.web.service;

import com.yunhwan.springbootcommunityweb.web.domain.Board;
import com.yunhwan.springbootcommunityweb.web.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<Board> findBoardList(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.
                    getPageNumber() - 1, pageable.getPageSize());

        return boardRepository.findAll(pageable);
    }

    public Board findBoardByIdx(Long idx) {
        return boardRepository.findById(idx).orElse(new Board()); // 옵션널 반환값..? 없다면 새 보드 객체 반환?
        // orElse는 null이 아닐때는 반환하지 않지만 new ...은 실행된다. -> orElseGet()으로 사용하면 해결가
    }
}
