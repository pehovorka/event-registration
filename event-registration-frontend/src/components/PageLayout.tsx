import { Col, Layout, Row } from "antd";
import { Footer as FooterContent } from "./footer";
import { useLocation } from "react-router-dom";
import { AppBar } from "./appbar";
import Title from "antd/lib/typography/Title";
const { Header, Content, Footer } = Layout;

type Props = {
  children: JSX.Element;
  title?: string;
  titleExtra?: JSX.Element;
};
function PageLayout({ children, title, titleExtra }: Props) {
  const location = useLocation();
  const { pathname } = location;

  const isAdminPath: boolean = pathname.startsWith("/admin/");

  if (isAdminPath) {
    document.title = title
      ? `${title} – Event registration app admin`
      : "Event registration app admin";
  } else {
    document.title = title
      ? `${title} – Event registration app`
      : "Event registration app";
  }
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Header
        style={{ padding: 0, ...(!isAdminPath && { background: "#fff" }) }}
      >
        <AppBar isAdminPath={isAdminPath} />
      </Header>
      <Content
        children={
          <>
            {title && (
              <Row
                justify="space-between"
                align="middle"
                style={{ marginBottom: "1rem" }}
              >
                <Col>
                  <Title level={2} style={{ margin: 0 }}>
                    {title}
                  </Title>
                </Col>
                {titleExtra && <Col>{titleExtra}</Col>}
              </Row>
            )}
            {children}
          </>
        }
        style={{ padding: "1rem 3rem" }}
      />
      <Footer>
        <FooterContent isAdminPath={isAdminPath} />
      </Footer>
    </Layout>
  );
}

export default PageLayout;
