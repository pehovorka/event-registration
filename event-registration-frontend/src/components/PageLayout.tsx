import { Layout } from "antd";
import { AppBar } from ".";
const { Header, Content } = Layout;

type Props = {
  children: JSX.Element;
  title?: string;
};
function PageLayout({ children, title }: Props) {
  document.title = title
    ? `${title} – Event registration app`
    : "Event registration app";
  return (
    <Layout>
      <Header style={{ padding: 0, backgroundColor: "#fff" }}>
        <AppBar />
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
