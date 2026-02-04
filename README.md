# DOCKin-backend

## 📂 Directory Structure 

<b>📂 백엔드 상세 폴더 구조 (Project Structure)</b>
<br />

```bash
[ DOCKin-spring ]
├── .github/workflows       # CI/CD 자동화 (GitHub Actions)
├── nginx/conf.d            # Nginx 리버스 프록시 및 서버 설정
├── src/main/java/com/DOCKin
│   ├── ai                  # AI 연동 모듈 (FastAPI, STT, 번역 연동)
│   │   ├── controller      # AI 기능 API 엔드포인트
│   │   ├── dto             # 데이터 전송 객체
│   │   ├── model           # AI 히스토리 및 로그 엔티티
│   │   └── service         # FastAPI 서비스 연동 로직
│   ├── attendance          # 근태 관리 시스템 (출퇴근 기록)
│   ├── chat                # 실시간 채팅 (WebSocket/STOMP 기반)
│   ├── member              # 회원 및 인증 시스템 (JWT/Security)
│   ├── global              # 글로벌 설정 (Security, WebSocket, Error Handling)
│   │   ├── config          # 주요 Bean 및 프로토콜 설정
│   │   ├── security        # JWT 기반 인증/인가 로직
│   │   └── util            # 오디오 컨버터 등 공통 유틸리티
│   ├── safetyCourse        # 안전 교육 관리 시스템
│   └── worklog             # 작업 일지 및 코멘트 시스템
├── src/main/resources
│   ├── application.properties  # 앱 환경 설정
│   └── schema.sql              # DB 스키마 정의
├── compose.yaml            # Docker 인프라 구성
└── Dockerfile              # 백엔드 컨테이너 빌드 설정

```



## 📖 API Documentation 

<b>🚀 상세 API 엔드포인트 보기 (Endpoints Specification)</b>
<br />

### 👤 인증 및 계정 (Auth)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/member/signup` | 회원가입 |
| `POST` | `/member/login` | 로그인 및 JWT 토큰 발급 |
| `POST` | `/member/logout` | 로그아웃 (토큰 무효화) |
| `DELETE` | `/member/{userId}` | 회원 탈퇴 |

### 🤖 AI 및 스마트 연동 (AI / STT)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/work-logs/stt` | **음성 파일 기반 작업일지 생성 (STT)** |
| `POST` | `/api/ai/rt-translate` | STT 실시간 번역 연동 |
| `POST` | `/api/ai/translate/{logId}` | 작업일지 다국어 번역 요청 |
| `POST` | `/api/ai/chatbot` | 현장 안전 가이드 챗봇 |

### 📝 작업일지 및 댓글 (Work Logs & Comments)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/work-logs` | 전체 작업일지 목록 조회 (Paging) |
| `POST` | `/api/work-logs` | 일반 텍스트 기반 일지 생성 |
| `GET` | `/api/work-logs/search` | 키워드 활용 게시물 검색 |
| `POST` | `/api/work-logs/{logId}/comments` | 관리자 피드백(댓글) 작성 |
| `PUT` | `/api/work-logs/{logId}` | 일지 내용 및 이미지 수정 |
| `DELETE` | `/api/work-logs/{logId}` | 작업일지 삭제 |

### 💬 실시간 소통 (Chat)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/chat/room` | 협업 채팅방 신규 생성 |
| `GET` | `/api/chat/rooms` | 참여 중인 모든 채팅방 목록 |
| `GET` | `/api/chat/room/{roomId}/messages` | 채팅 내역 조회 (무한 스크롤) |
| `DELETE` | `/api/chat/room/leave/{roomId}` | 채팅방 나가기 |

### ⏰ 근태 및 안전 교육 (Attendance & Safety)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/attendance/in` | 출근 기록 등록 (위치/시간) |
| `POST` | `/api/attendance/out` | 퇴근 기록 등록 |
| `GET` | `/api/safety/user/training/uncompleted` | 미이수 안전 교육 목록 확인 |
| `PATCH` | `/api/safety/user/training/complete` | 교육 영상 이수 완료 처리 |


## 📂 Database 

<b>📊 MySQL</b>
<br />






