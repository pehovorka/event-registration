import { useCallback, useEffect, useState } from "react";
import axios, { AxiosInstance } from "axios";
import { API_BASE_URL } from "../config/api";
import { useApi } from "../services/api";

export enum Methods {
  get,
  post,
  delete,
}

interface Props {
  method: Methods;
  path: string;
  body?: any;
  withAdminAuth?: boolean;
}

const useFetchData = <T>({ method, path, body, withAdminAuth }: Props) => {
  const [data, setData] = useState<T | undefined>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown | null>(null);
  const { api, authApi } = useApi();

  const fetchData = useCallback(
    async (fetcher: AxiosInstance) => {
      try {
        let response;
        method === Methods.get &&
          ({ data: response } = await fetcher.get<T>(`${API_BASE_URL}${path}`));
        method === Methods.post &&
          ({ data: response } = await fetcher.post<T>(
            `${API_BASE_URL}${path}`,
            body
          ));
        method === Methods.delete &&
          ({ data: response } = await fetcher.delete<T>(
            `${API_BASE_URL}${path}`,
            {
              data: body,
            }
          ));
        setData(response);
      } catch (error) {
        if (axios.isAxiosError(error)) {
          setError(error);
          console.error(error);
        } else {
          console.error("Fetch data error: ", error);
        }
      }
      setLoading(false);
    },
    [body, method, path]
  );

  useEffect(() => {
    if (withAdminAuth) {
      fetchData(authApi);
    } else {
      fetchData(api);
    }
  }, [method, path, body, fetchData, withAdminAuth, api, authApi]);

  const refetch = () => {
    if (withAdminAuth) {
      fetchData(authApi);
    } else {
      fetchData(api);
    }
  };

  return {
    data,
    loading,
    error,
    refetch,
  };
};

export default useFetchData;
