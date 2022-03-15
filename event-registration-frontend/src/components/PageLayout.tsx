import { Layout } from "antd";
import { AppBar } from ".";
const { Header, Content } = Layout;

type Props = {
  children: JSX.Element;
};
function PageLayout({ children }: Props) {
  return (
    <Layout>
      <Header style={{ padding: 0, backgroundColor: "#fff" }}>
        <AppBar />
      </Header>
      <Content children={children} style={{ padding: "1rem 3rem" }} />
    </Layout>
  );
}

export default PageLayout;
