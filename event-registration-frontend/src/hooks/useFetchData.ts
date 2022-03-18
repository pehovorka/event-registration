import { useCallback, useEffect, useState } from "react";
import axios from "axios";
import { API_BASE_URL } from "../config/api";

export enum Methods {
  get,
  post,
  delete,
}

interface Props {
  method: Methods;
  path: string;
  body?: any;
}

const useFetchData = <T>({ method, path, body }: Props) => {
  const [data, setData] = useState<T | undefined>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown | null>(null);

  const fetchData = useCallback(async () => {
    try {
      let response;
      method === Methods.get &&
        ({ data: response } = await axios.get<T>(`${API_BASE_URL}${path}`));
      method === Methods.post &&
        ({ data: response } = await axios.post<T>(
          `${API_BASE_URL}${path}`,
          body
        ));
      method === Methods.delete &&
        ({ data: response } = await axios.delete<T>(
          `${API_BASE_URL}${path}`,
          body
        ));
      setData(response);
    } catch (error) {
      setError(error);
      console.error(error);
    }
    setLoading(false);
  }, [body, method, path]);

  useEffect(() => {
    fetchData();
  }, [method, path, body, fetchData]);

  const refetch = () => {
    fetchData();
  };

  return {
    data,
    loading,
    error,
    refetch,
  };
};

export default useFetchData;
