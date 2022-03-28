import { Layout } from "antd";
import { useLocation } from "react-router-dom";
import { AppBar } from "./appbar";
const { Header, Content } = Layout;

type Props = {
  children: JSX.Element;
  title?: string;
};
function PageLayout({ children, title }: Props) {
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
    <Layout>
      <Header
        style={{ padding: 0, ...(!isAdminPath && { background: "#fff" }) }}
      >
        <AppBar isAdminPath={isAdminPath} />
      </Header>
      <Content
        children={
          <>
            {title && <h1>{title}</h1>}
            {children}
          </>
        }
        style={{ padding: "1rem 3rem" }}
      />
    </Layout>
  );
}

export default PageLayout;
