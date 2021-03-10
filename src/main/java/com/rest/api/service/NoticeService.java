package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.Notice;
import com.rest.api.entity.fastcafe_log.LogNoticeRead;
import com.rest.api.repository.fastcafe_admin.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final LogService logService;


    @Transactional
    public Page<Notice> findAll(int page, int size, Integer admin_id) {
        Page<Notice> notices = noticeRepository.findByStat("1000", PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id")));
        if(!notices.isEmpty()){
            // 조회 여부 확인
            notices.map(notice -> {
                notice.setIsRead(logService.findByNoticeIdAndAdminId(notice.getId(), admin_id).orElseGet(LogNoticeRead::new));
                return notice;
            });
        }
        return notices;
    }

    @Transactional
    public Optional<Notice> readNotice(Integer notice_id, Integer admin_id) {
        Optional<Notice> notice = noticeRepository.findById(notice_id);
        if(notice.isPresent()){
            // 조회수 증가
            Notice n = notice.get();
            n.setReadCnt(n.getReadCnt() + 1);
            noticeRepository.save(n);

            // 조회 기록 확인
            LogNoticeRead logNoticeRead = logService.findByNoticeIdAndAdminId(notice_id, admin_id).orElseGet(LogNoticeRead::new);

            if(logNoticeRead.getId() != 0){
                logNoticeRead= logNoticeRead
                        .withReadCnt(logNoticeRead.getReadCnt() + 1);
            } else {
                logNoticeRead = LogNoticeRead.builder()
                        .noticeId(notice_id)
                        .adminId(admin_id)
                        .readCnt(1)
                        .build();
            }
            // 조회 기록 저장
            logNoticeRead = logService.saveLogNoticeRead(logNoticeRead);
            n.setIsRead(logNoticeRead);
        }

        return notice;
    }
}
