import { Pagination } from 'react-bootstrap';
import './BlogPage.scss';
const BlogPage = () => {
    return (
    <div>
    <div className="blog-container">
      <div className="blog-card">
         <img src={require("../assets/img/test.png")}   />
        <div className="blog-card-content">
          <h3>EduHealth có thể giảm tình trạng vắng mặt như thế nào với EHR tiên tiến</h3>
          <p className="blog-meta">Bởi Y tế học đường • Ngày 28 tháng 10 năm 2024</p>
          <p className="blog-excerpt">
            EduHealth có thể giảm tình trạng vắng mặt của học sinh là một thách thức trên toàn thế giới mà các trường học phải đối mặt liên tục. Thách thức này ảnh hưởng đến sức khỏe tổng thể của trẻ em về thành tích học tập và sự tham gia trong lớp học. Một số trường hợp vắng mặt là không thể tránh khỏi do các vấn đề sức khỏe, chúng ta không thể tránh khỏi. Tuy nhiên, chúng ta có thể giảm thiểu những trường hợp khác bằng...
          </p>
          <a href="https://www.eduhealthsystem.com/blog/how-eduhealth-can-reduce-absenteeism-with-advanced-ehr/" className="read-more-btn">Đọc thêm</a>
        </div>
      </div>
    </div>
    <div className="blog-container ">
     <div class="blog-card">
        <img src={require("../assets/img/test3.png")}   />
        <div class="blog-card-content">
            <h3>Tương lai khỏe mạnh hơn: Tận dụng dữ liệu lớn trong phần mềm sức khỏe trường học </h3>
            <p class="blog-meta">Bởi Y tế học đường • Ngày 11 tháng 9 năm 2024</p>
            <p class="blog-excerpt">
                Tương lai khỏe mạnh hơn: Tận dụng dữ liệu lớn trong phần mềm sức khỏe trường học Từ tư vấn tâm lý đến theo dõi vắc-xin, các chương trình sức khỏe trường học đã đi một chặng đường dài. Bây giờ, phần mềm sức khỏe trường học đã sẵn sàng khám phá tiềm năng của dữ liệu lớn.
                 Dữ liệu lớn là thuật ngữ chung cho cả dữ liệu có cấu trúc và không có cấu trúc…
            </p>
            <a href="https://www.eduhealthsystem.com/blog/a-healthier-future-leveraging-big-data-in-school-health-software/" class="read-more-btn">Đọc thêm</a>
        </div>
    </div>
    </div>
    <div className="blog-container">
    <div className="blog-card">
        <img src={require("../assets/img/test1.png")}   />
        <div className="blog-card-content">
          <h3>Ngoài lớp học: Tác động của phần mềm hồ sơ sức khỏe điện tử đến sức khỏe cộng đồng trường học</h3>
          <p className="blog-meta">Bởi Y tế học đường • Ngày 15 tháng 8 năm 2024</p>
          <p className="blog-excerpt">
            Ngoài lớp học: Tác động của phần mềm hồ sơ sức khỏe điện tử đến sức khỏe cộng đồng trường học Giới thiệu & Sự phát triển của Quản lý sức khỏe tại trường học Sự phát triển của quản lý sức khỏe tại trường học và sức khỏe của học sinh đã chứng kiến ​​vai trò quan trọng của phần mềm Hồ sơ sức khỏe điện tử (EHR).
             EHR đã trở thành một công cụ thiết yếu…
          </p>
          <a href="https://www.eduhealthsystem.com/blog/impact-of-ehr-software-on-school-community-health/" className="read-more-btn">Đọc thêm</a>
        </div>
      </div>
      </div>
      <Pagination count={10} shape="rounded" />
</div>
  );
};
export default BlogPage;

