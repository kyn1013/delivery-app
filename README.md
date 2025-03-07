## ERD
![Image](https://github.com/user-attachments/assets/8ec8aa07-bc29-4375-ae94-da43c84a6f9f)

## 담당기능 : 장바구니 및 주문
* 장바구니 기능 및 주문 기능
* 주문 전에 메뉴의 제고 체크 후 해당 재고가 유효한지 확인 후 주문 가능
* 주문이 배달완료 상태가 되어야 리뷰 작성이 가능
* 주문 상태 변경시 해당 조건에 충족하는지 검증
* 주문 상태는 enum으로 관리
 
# API 명세

## 주문(Order) API

| HTTP Method | 기능 | 엔드포인트 | 인증 | 요청 예시 | 응답 예시 | 성공 코드 | 실패 코드 |
|-------------|-----------------|---------------|-------------|--------------------------------------------------|--------------------------------------------------|----------|----------|
| POST | 주문 등록 | `/api/v1/orders` | JWT 토큰 | `{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double, "orderDetailResponseDtos": [{ "orderId": Long, "menuId": Long, "menuName": String, "quantity": Integer, "price": Double }] }` | `{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double, "orderDetailResponseDtos": [...] }` | 200 | 400, 403, 404 |
| GET | 주문 단건 조회 | `/api/v1/orders/{orderId}` | JWT 토큰 | - | `{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double, "orderDetailResponseDtos": [...] }` | 200 | 400, 403, 404 |
| GET | 주문 전체 조회 (자신이 한 주문) | `/api/v1/orders` | JWT 토큰 | - | `{ "content": [{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double }], "page": { "size": Integer, "number": Integer, "totalElements": Long, "totalPages": Integer } }` | 200 | 400, 403, 404 |
| PATCH | 주문 수락 | `/api/v1/orders/{orderId}/accept` | JWT 토큰 | - | `{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double }` | 200 | 400, 403, 404 |
| PATCH | 주문 거절 | `/api/v1/orders/{orderId}/reject` | JWT 토큰 | - | `{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double }` | 200 | 400, 403, 404 |
| PATCH | 주문 취소 | `/api/v1/orders/{orderId}/cancel` | JWT 토큰 | - | `{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double }` | 200 | 400, 403, 404 |
| PATCH | 배달 중 | `/api/v1/orders/{orderId}/delivering` | JWT 토큰 | - | `{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double }` | 200 | 400, 403, 404 |
| PATCH | 배달 완료 | `/api/v1/orders/{orderId}/complete` | JWT 토큰 | - | `{ "orderId": Long, "orderNumber": String, "memberName": String, "storeName": String, "state": String, "totalPrice": Double }` | 200 | 400, 403, 404 |

## 장바구니(Cart) API

| HTTP Method | 기능 | 엔드포인트 | 인증 | 요청 예시 | 응답 예시 | 성공 코드 | 실패 코드 |
|-------------|-----------------|---------------|-------------|--------------------------------------------------|--------------------------------------------------|----------|----------|
| POST | 장바구니 추가 | `/api/v1/carts` | JWT 토큰 (일반 사용자) | `{ "menuId": Long, "quantity": Long }` | `{ "cartId": Long, "menuId": Long, "menuName": String, "memberId": Long, "memberName": String, "quantity": Long }` | 200 | 400, 403, 404 |
| GET | 장바구니 목록 조회 | `/api/v1/carts` | JWT 토큰 (일반 사용자) | - | `{ "content": [{ "cartId": Long, "menuId": Long, "menuName": String, "memberId": Long, "memberName": String, "quantity": Long }], "page": { "size": Long, "number": Long, "totalElements": Long, "totalPages": Long } }` | 200 | 400, 403, 404 |
| PATCH | 장바구니 수정 | `/api/v1/carts/{cartId}` | JWT 토큰 (일반 사용자) | `{ "menuId": Long, "quantity": Long }` | `{ "cartId": Long, "menuId": Long, "menuName": String, "memberId": Long, "memberName": String, "quantity": Long }` | 200 | 400, 403, 404 |
| DELETE | 장바구니 삭제 | `/api/v1/carts/{cartId}` | JWT 토큰 (일반 사용자) | - | - | 200 | 400, 403, 404 |

## 공통 실패 응답 형식

```json
{
  "message": String,
  "status": Long
}
```

