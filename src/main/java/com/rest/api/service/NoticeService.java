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
            notices.map(notice -> {
                notice.setIsRead(logService.findByNoticeIdAndAdminId(notice.getId(), admin_id).orElseGet(LogNoticeRead::new));
                return notice;
            });
        }
        return notices;
    }

    @Transactional
    public Optional<Notice> findById(Integer notice_id, Integer admin_id) {
        Optional<Notice> notice = noticeRepository.findById(notice_id);
        if(notice.isPresent()){
            Notice n = notice.get();
            n.setReadCnt(n.getReadCnt() + 1);
            noticeRepository.save(n);

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
            logNoticeRead = logService.saveLogNoticeRead(logNoticeRead);
            n.setIsRead(logNoticeRead);
        }

        return notice;
    }
}
